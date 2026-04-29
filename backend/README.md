# Acadexia Backend

API REST Node.js + Express pour l'application Acadexia - Assistant Intelligent Étudiant

## Technologies

- **Runtime:** Node.js
- **Framework:** Express.js
- **Base de données:** MongoDB + Mongoose
- **Authentification:** JWT + bcryptjs
- **IA:** Google Gemini API

## Installation

### Prérequis

- Node.js 18+
- MongoDB Atlas (compte gratuit)
- Clé API Google Gemini

### Étapes

```bash
# 1. Installer les dépendances
npm install

# 2. Créer le fichier .env
# Ajouter:
# PORT=5000
# MONGODB_URI=<your_mongodb_connection_string>
# JWT_SECRET=<your_secret_key>
# GEMINI_API_KEY=<your_gemini_key>
# NODE_ENV=development

# 3. Lancer le serveur
npm start

# Pour le développement (avec nodemon)
npm run dev
```

## Structure

```
src/
├── models/          # Schémas MongoDB
├── routes/          # Routes API
├── controllers/     # Logique métier
├── middleware/      # Auth, etc.
├── utils/          # Fonctions utilitaires (Gemini)
├── config/         # Configuration BD
└── server.js       # Point d'entrée
```

## Endpoints API

### Authentification
- `POST /api/auth/signup` - Inscription
- `POST /api/auth/login` - Connexion

### Cours
- `GET /api/courses` - Lister les cours
- `POST /api/courses` - Créer un cours
- `GET /api/courses/:courseId` - Détails d'un cours
- `PUT /api/courses/:courseId` - Mettre à jour
- `DELETE /api/courses/:courseId` - Supprimer

### IA
- `POST /api/ai/chat` - Chat avec l'IA
- `POST /api/ai/summary/:courseId` - Générer résumé
- `POST /api/ai/quiz/generate` - Générer quiz
- `POST /api/ai/quiz/submit` - Soumettre réponse

### Tâches
- `GET /api/tasks` - Lister les tâches
- `POST /api/tasks` - Créer une tâche
- `POST /api/tasks/plan/generate` - Générer planning

### Progression
- `POST /api/progress/study-time` - Enregistrer temps d'étude
- `GET /api/progress` - Voir progression
- `GET /api/progress/stats` - Statistiques

## Configuration MongoDB

1. Créer un compte gratuit sur [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Créer un cluster
3. Obtenir la connection string
4. Remplacer dans `.env`

## Gemini API

1. Aller sur [Google AI Studio](https://aistudio.google.com)
2. Créer une clé API gratuite
3. Ajouter à `.env`

## Sécurité

⚠️ **Avant de déployer en production:**
- Changer `JWT_SECRET` par une clé forte
- Utiliser HTTPS
- Valider toutes les entrées utilisateur
- Implémenter rate limiting
