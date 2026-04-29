# Guide de Test - Acadexia

## ✅ Tester le Backend

### 1. Setup Initial
```bash
cd backend
npm install
npm run dev
```

Vérifier: `GET http://localhost:5000/api/health`
→ Doit retourner: `{"message": "Acadexia Backend is running!"}`

### 2. Authentification

**Inscription:**
```bash
POST http://localhost:5000/api/auth/signup
Body:
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Connexion:**
```bash
POST http://localhost:5000/api/auth/login
Body:
{
  "email": "john@example.com",
  "password": "password123"
}
```

Réponse attendue:
```json
{
  "token": "eyJhbGc...",
  "user": {
    "id": "...",
    "name": "John Doe",
    "email": "john@example.com",
    "xp": 0,
    "level": 1
  }
}
```

**💡 Copier le token pour les requêtes suivantes**

### 3. Créer un Cours

```bash
POST http://localhost:5000/api/courses
Headers:
  Authorization: Bearer <TOKEN>
Body:
{
  "title": "Mathématiques",
  "subject": "Math",
  "content": "Les dérivées permettent de calculer le taux de variation d'une fonction..."
}
```

### 4. Chat IA

```bash
POST http://localhost:5000/api/ai/chat
Headers:
  Authorization: Bearer <TOKEN>
Body:
{
  "message": "Explique-moi les dérivées simplement"
}
```

Réponse: Gemini répond avec une explication simple

### 5. Générer un Résumé

```bash
POST http://localhost:5000/api/ai/summary/{courseId}
Headers:
  Authorization: Bearer <TOKEN>
```

Réponse: Résumé structuré du contenu du cours

### 6. Générer un Quiz

```bash
POST http://localhost:5000/api/ai/quiz/generate
Headers:
  Authorization: Bearer <TOKEN>
Body:
{
  "courseId": "{courseId}",
  "numQuestions": 5
}
```

Réponse: Quiz avec questions et options

### 7. Soumettre une Réponse

```bash
POST http://localhost:5000/api/ai/quiz/submit
Headers:
  Authorization: Bearer <TOKEN>
Body:
{
  "quizId": "{quizId}",
  "questionIndex": 0,
  "userAnswer": "A"
}
```

### 8. Créer une Tâche

```bash
POST http://localhost:5000/api/tasks
Headers:
  Authorization: Bearer <TOKEN>
Body:
{
  "title": "Étudier les dérivées",
  "subject": "Math",
  "priority": "high",
  "estimatedTime": 2
}
```

### 9. Générer un Planning

```bash
POST http://localhost:5000/api/tasks/plan/generate
Headers:
  Authorization: Bearer <TOKEN>
```

Réponse: Planning optimisé par jour

### 10. Enregistrer Temps d'Étude

```bash
POST http://localhost:5000/api/progress/study-time
Headers:
  Authorization: Bearer <TOKEN>
Body:
{
  "subject": "Math",
  "dailyMinutes": 60
}
```

Réponse:
```json
{
  "xp": 600,
  "level": 7,
  "streak": 1,
  "badges": ["Week Warrior"]
}
```

## 🧪 Tester le Frontend (Android)

### Étapes

1. **Ouvrir Android Studio**
   ```
   File > Open > frontend/
   ```

2. **Configuration API**
   - Modifier `ApiClient.kt`
   - Changer `BASE_URL` si nécessaire
   ```kotlin
   private const val BASE_URL = "http://10.0.2.2:5000/api/"
   ```

3. **Lancer l'émulateur**
   ```
   Tools > AVD Manager > Run
   ```

4. **Compiler et exécuter**
   ```
   Run > Run 'app'
   ```

### Flux de Test

#### Login/Signup
- [ ] Écran login visible
- [ ] Pouvoir passer à signup
- [ ] Créer un compte
- [ ] Se connecter avec les identifiants
- [ ] JWT token reçu

#### Dashboard
- [ ] XP et level affichés
- [ ] Streak visible
- [ ] Boutons de navigation visibles

#### Chat IA
- [ ] Pouvoir écrire un message
- [ ] Réponse de Gemini reçue
- [ ] Messages affichés correctement

#### Courses
- [ ] Lister les cours
- [ ] Ajouter un nouveau cours
- [ ] Voir le contenu

#### Tasks
- [ ] Lister les tâches
- [ ] Créer une tâche
- [ ] Voir statut et priorité

#### Progress
- [ ] Voir statistiques
- [ ] Badges affichés
- [ ] Temps d'étude par matière

## 🐛 Troubleshooting

### "Failed to connect to backend"
- Vérifier que backend tourne: `npm run dev`
- Vérifier l'URL API dans `ApiClient.kt`
- Sur émulateur: utiliser `10.0.2.2` au lieu de `localhost`

### "Invalid token"
- Régénérer le token avec login
- Vérifier `JWT_SECRET` dans `.env`

### "Gemini API error"
- Vérifier la clé API dans `.env`
- Vérifier le quota gratuit Gemini

### "MongoDB connection error"
- Vérifier `MONGODB_URI` dans `.env`
- Vérifier que IP whitelist inclut votre adresse
- Tester la connexion avec Mongo Compass

## 📊 Exemple de Données Complètes

Après une session complète:
- User: Level 10, 950 XP, 7 jour streak
- 3 courses avec résumés
- 2 quizzes avec scores 85%, 90%
- 5 tâches (3 done, 2 in progress)
- 15 heures d'étude total

## ✨ Checks Finaux

- [ ] Backend API sur `localhost:5000`
- [ ] Authentification JWT fonctionne
- [ ] Gemini génère réponses < 3s
- [ ] MongoDB sauvegarde les données
- [ ] Android app se connecte à l'API
- [ ] Tous les écrans s'affichent
- [ ] Données persistes après fermeture

---

**Prêt pour tester!** 🚀
