const express = require('express');
const authMiddleware = require('../middleware/auth');
const {
  chatbot,
  generateSummary,
  generateQuiz,
  submitQuizAnswer,
} = require('../controllers/aiController');

const router = express.Router();

router.use(authMiddleware);

router.post('/chat', chatbot);
router.post('/summary/:courseId', generateSummary);
router.post('/quiz/generate', generateQuiz);
router.post('/quiz/submit', submitQuizAnswer);

module.exports = router;
