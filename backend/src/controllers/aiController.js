const { callGemini } = require('../utils/gemini');
const Course = require('../models/Course');
const Quiz = require('../models/Quiz');

const chatbot = async (req, res) => {
  try {
    const { message } = req.body;
    const userId = req.userId;

    const prompt = `Tu es un assistant educatif intelligent pour aider les étudiants. Réponds de manière claire, concise et pédagogique à cette question: ${message}`;

    const response = await callGemini(prompt);

    res.json({
      message,
      response,
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const generateSummary = async (req, res) => {
  try {
    const { courseId } = req.params;
    const userId = req.userId;

    const course = await Course.findById(courseId);
    if (!course || course.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Course not found' });
    }

    const prompt = `Fais un résumé structuré et concis du texte suivant en mettant en évidence les points clés:\n\n${course.content}`;

    const summary = await callGemini(prompt);

    course.summary = summary;
    await course.save();

    res.json({
      courseId,
      summary,
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const generateQuiz = async (req, res) => {
  try {
    const { courseId, numQuestions = 5 } = req.body;
    const userId = req.userId;

    const course = await Course.findById(courseId);
    if (!course || course.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Course not found' });
    }

    const prompt = `Crée ${numQuestions} questions de quiz (MCQ) basées sur le contenu suivant. Format JSON strict avec cette structure:
{
  "questions": [
    {
      "question": "texte question",
      "options": ["A", "B", "C", "D"],
      "correctAnswer": "A",
      "type": "mcq"
    }
  ]
}

Contenu: ${course.content}`;

    const response = await callGemini(prompt);
    const jsonMatch = response.match(/\{[\s\S]*\}/);
    const quizData = jsonMatch ? JSON.parse(jsonMatch[0]) : { questions: [] };

    const quiz = new Quiz({
      courseId,
      userId,
      title: `Quiz: ${course.title}`,
      subject: course.subject,
      questions: quizData.questions,
      totalQuestions: quizData.questions.length,
    });

    await quiz.save();

    res.status(201).json({
      quizId: quiz._id,
      questions: quizData.questions.map(q => ({
        question: q.question,
        options: q.options,
        type: q.type,
      })),
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const submitQuizAnswer = async (req, res) => {
  try {
    const { quizId, questionIndex, userAnswer } = req.body;
    const userId = req.userId;

    const quiz = await Quiz.findById(quizId);
    if (!quiz || quiz.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Quiz not found' });
    }

    const question = quiz.questions[questionIndex];
    question.userAnswer = userAnswer;
    question.isCorrect = userAnswer === question.correctAnswer;

    const score = quiz.questions.filter(q => q.isCorrect).length;
    quiz.score = score;
    quiz.percentage = Math.round((score / quiz.totalQuestions) * 100);

    await quiz.save();

    res.json({
      isCorrect: question.isCorrect,
      correctAnswer: question.correctAnswer,
      score: quiz.score,
      percentage: quiz.percentage,
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = {
  chatbot,
  generateSummary,
  generateQuiz,
  submitQuizAnswer,
};
