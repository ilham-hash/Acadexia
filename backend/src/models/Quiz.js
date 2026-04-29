const mongoose = require('mongoose');

const quizSchema = new mongoose.Schema({
  courseId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Course',
    required: true,
  },
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
  },
  title: String,
  subject: String,
  questions: [
    {
      question: String,
      type: {
        type: String,
        enum: ['mcq', 'open'],
      },
      options: [String],
      correctAnswer: String,
      userAnswer: String,
      isCorrect: Boolean,
    },
  ],
  score: Number,
  totalQuestions: Number,
  percentage: Number,
  completedAt: {
    type: Date,
    default: Date.now,
  },
});

module.exports = mongoose.model('Quiz', quizSchema);
