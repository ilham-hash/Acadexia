# Acadexia - Assistant Intelligent Étudiant 🎓

Application mobile intelligente (Android) + Backend API (Node.js) pour aider les étudiants

## 🚀 Démarrage Rapide

### Backend
```bash
cd backend
npm install
npm run dev
# API sur http://localhost:5000
```

### Frontend (Android)
Ouvrir `frontend/` avec Android Studio et lancer l'émulateur

## 📋 Architecture

- **Backend:** Node.js + Express + MongoDB
- **Frontend:** Android + Kotlin + Jetpack Compose
- **IA:** Google Gemini API
- **Auth:** JWT + bcryptjs

## ✨ Fonctionnalités

- 💬 Chat IA éducatif
- 📚 Gestion des cours
- ✅ Planification intelligente
- 📊 Suivi de progression
- 🏆 Système de gamification (XP, levels, badges)
- 🎓 Génération auto de résumés et quiz

## 📂 Structure du Projet

```
Acadexia/
├── backend/
│   ├── src/
│   │   ├── models/       # MongoDB schemas
│   │   ├── controllers/  # Business logic
│   │   ├── routes/       # API endpoints
│   │   ├── middleware/   # Auth, etc
│   │   └── utils/        # Gemini API
│   ├── package.json
│   └── README.md
└── frontend/
    └── app/src/main/java/com/acadexia/
        ├── network/      # API client
        └── ui/           # Screens
```

## 🔐 Configuration

Créer `backend/.env`:
```
PORT=5000
MONGODB_URI=your_mongodb_string
JWT_SECRET=your_secret
GEMINI_API_KEY=your_key
NODE_ENV=development
```

## 📚 Documentation

- [Backend README](backend/README.md) - API complète
- Code comments et structure claire

## 🎯 Statut

✅ Phase 1 - Backend & Frontend structure complète
⏳ Phase 2 - Tests et optimisations
🔄 Améliorations continues

**Version:** 1.0.0
**Dernière mise à jour:** 2024