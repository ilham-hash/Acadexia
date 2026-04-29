const User = require('../models/User');
const Progress = require('../models/Progress');
const Quiz = require('../models/Quiz');

const updateStudyTime = async (req, res) => {
  try {
    const { subject, dailyMinutes } = req.body;
    const userId = req.userId;

    let progress = await Progress.findOne({ userId, subject });

    if (!progress) {
      progress = new Progress({ userId, subject, dailyStudyTime: dailyMinutes });
    } else {
      progress.dailyStudyTime = (progress.dailyStudyTime || 0) + dailyMinutes;
      progress.totalStudyTime = (progress.totalStudyTime || 0) + dailyMinutes;
    }

    await progress.save();

    const user = await User.findById(userId);
    const xpGain = dailyMinutes * 10;
    user.xp = (user.xp || 0) + xpGain;
    user.level = Math.floor(user.xp / 100) + 1;

    const today = new Date().toDateString();
    const lastStudy = user.lastStudyDate ? new Date(user.lastStudyDate).toDateString() : null;

    if (lastStudy !== today) {
      if (lastStudy === new Date(Date.now() - 86400000).toDateString()) {
        user.streak = (user.streak || 0) + 1;
      } else {
        user.streak = 1;
      }
      user.lastStudyDate = new Date();
    }

    if (user.streak === 7 && !user.badges?.includes('Week Warrior')) {
      user.badges.push('Week Warrior');
    }
    if (user.level >= 10 && !user.badges?.includes('Level 10')) {
      user.badges.push('Level 10');
    }

    await user.save();

    res.json({
      message: 'Study time updated',
      xp: user.xp,
      level: user.level,
      streak: user.streak,
      badges: user.badges,
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getProgress = async (req, res) => {
  try {
    const userId = req.userId;
    const progress = await Progress.find({ userId });
    const user = await User.findById(userId);

    res.json({
      progress,
      xp: user.xp,
      level: user.level,
      streak: user.streak,
      badges: user.badges,
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getStats = async (req, res) => {
  try {
    const userId = req.userId;
    const user = await User.findById(userId);
    const quizzes = await Quiz.find({ userId });
    const progress = await Progress.find({ userId });

    const totalStudyTime = progress.reduce((sum, p) => sum + (p.totalStudyTime || 0), 0);
    const avgQuizScore = quizzes.length > 0 ? Math.round(quizzes.reduce((sum, q) => sum + (q.percentage || 0), 0) / quizzes.length) : 0;

    res.json({
      level: user.level,
      xp: user.xp,
      streak: user.streak,
      badges: user.badges,
      totalStudyTime,
      totalQuizzes: quizzes.length,
      avgQuizScore,
      subjects: progress.map(p => ({ subject: p.subject, studyTime: p.totalStudyTime })),
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = {
  updateStudyTime,
  getProgress,
  getStats,
};
