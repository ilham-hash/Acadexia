const express = require('express');
const authMiddleware = require('../middleware/auth');
const {
  createTask,
  getTasks,
  updateTask,
  deleteTask,
  generatePlan,
} = require('../controllers/taskController');

const router = express.Router();

router.use(authMiddleware);

router.post('/', createTask);
router.get('/', getTasks);
router.put('/:taskId', updateTask);
router.delete('/:taskId', deleteTask);
router.post('/plan/generate', generatePlan);

module.exports = router;
