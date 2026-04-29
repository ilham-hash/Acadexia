const Course = require('../models/Course');

const createCourse = async (req, res) => {
  try {
    const { title, subject, content } = req.body;
    const userId = req.userId;

    const course = new Course({
      userId,
      title,
      subject,
      content,
    });

    await course.save();
    res.status(201).json({ message: 'Course created', course });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getCourses = async (req, res) => {
  try {
    const userId = req.userId;
    const courses = await Course.find({ userId });
    res.json(courses);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getCourse = async (req, res) => {
  try {
    const { courseId } = req.params;
    const userId = req.userId;

    const course = await Course.findById(courseId);
    if (!course || course.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Course not found' });
    }

    res.json(course);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const updateCourse = async (req, res) => {
  try {
    const { courseId } = req.params;
    const userId = req.userId;
    const { title, subject, content } = req.body;

    const course = await Course.findById(courseId);
    if (!course || course.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Course not found' });
    }

    course.title = title || course.title;
    course.subject = subject || course.subject;
    course.content = content || course.content;

    await course.save();
    res.json({ message: 'Course updated', course });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const deleteCourse = async (req, res) => {
  try {
    const { courseId } = req.params;
    const userId = req.userId;

    const course = await Course.findById(courseId);
    if (!course || course.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Course not found' });
    }

    await Course.findByIdAndDelete(courseId);
    res.json({ message: 'Course deleted' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = {
  createCourse,
  getCourses,
  getCourse,
  updateCourse,
  deleteCourse,
};
