const express = require('express');
const authMiddleware = require('../middleware/auth');
const {
  updateStudyTime,
  getProgress,
  getStats,
} = require('../controllers/progressController');

const router = express.Router();

router.use(authMiddleware);

router.post('/study-time', updateStudyTime);
router.get('/', getProgress);
router.get('/stats', getStats);

module.exports = router;
