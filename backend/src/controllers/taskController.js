const Task = require('../models/Task');
const { callGemini } = require('../utils/gemini');

const createTask = async (req, res) => {
  try {
    const { title, description, subject, deadline, priority, estimatedTime } = req.body;
    const userId = req.userId;

    const task = new Task({
      userId,
      title,
      description,
      subject,
      deadline,
      priority,
      estimatedTime,
    });

    await task.save();
    res.status(201).json({ message: 'Task created', task });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getTasks = async (req, res) => {
  try {
    const userId = req.userId;
    const tasks = await Task.find({ userId }).sort({ deadline: 1 });
    res.json(tasks);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const updateTask = async (req, res) => {
  try {
    const { taskId } = req.params;
    const userId = req.userId;
    const { title, status, priority } = req.body;

    const task = await Task.findById(taskId);
    if (!task || task.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Task not found' });
    }

    task.title = title || task.title;
    task.status = status || task.status;
    task.priority = priority || task.priority;

    await task.save();
    res.json({ message: 'Task updated', task });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const deleteTask = async (req, res) => {
  try {
    const { taskId } = req.params;
    const userId = req.userId;

    const task = await Task.findById(taskId);
    if (!task || task.userId.toString() !== userId) {
      return res.status(404).json({ error: 'Task not found' });
    }

    await Task.findByIdAndDelete(taskId);
    res.json({ message: 'Task deleted' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const generatePlan = async (req, res) => {
  try {
    const userId = req.userId;
    const tasks = await Task.find({ userId, status: { $ne: 'completed' } });

    const taskList = tasks.map(t => `- ${t.title} (deadline: ${t.deadline}, temps estimé: ${t.estimatedTime}h)`).join('\n');

    const prompt = `Tu es un expert en planification d'études. Crée un planning d'étude optimisé pour ces tâches:
${taskList}

Fournis un planning jour par jour avec les heures recommandées pour chaque tâche. Format JSON avec cette structure:
{
  "plan": [
    {"day": "Lundi", "tasks": [{"name": "...", "hours": 2}]}
  ],
  "recommendation": "conseil général"
}`;

    const response = await callGemini(prompt);
    const jsonMatch = response.match(/\{[\s\S]*\}/);
    const plan = jsonMatch ? JSON.parse(jsonMatch[0]) : { plan: [], recommendation: '' };

    res.json(plan);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

module.exports = {
  createTask,
  getTasks,
  updateTask,
  deleteTask,
  generatePlan,
};
