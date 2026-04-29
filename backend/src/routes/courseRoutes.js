const express = require('express');
const authMiddleware = require('../middleware/auth');
const {
  createCourse,
  getCourses,
  getCourse,
  updateCourse,
  deleteCourse,
} = require('../controllers/courseController');

const router = express.Router();

router.use(authMiddleware);

router.post('/', createCourse);
router.get('/', getCourses);
router.get('/:courseId', getCourse);
router.put('/:courseId', updateCourse);
router.delete('/:courseId', deleteCourse);

module.exports = router;
