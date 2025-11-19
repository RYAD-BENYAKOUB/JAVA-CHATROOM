# JAVA-CHATROOM
Voici un **README complet et propre** pour ton projet Java Chatroom utilisant l’architecture **Client / Serveur (C/S)**.
Tu peux le copier directement dans un fichier : **README.md**

---

# 📌 **README.md — JAVA CHATROOM (Architecture Client / Serveur)**

```markdown
# 💬 Java Chatroom — Architecture Client / Serveur (C/S)

Une application de **messagerie instantanée** développée en Java utilisant l’architecture **Client/Serveur**.  
Le projet permet à plusieurs utilisateurs de se connecter à un serveur, d'envoyer et de recevoir des messages en temps réel.

---

## 🚀 Fonctionnalités

- Connexion de plusieurs clients au serveur
- Envoi et réception de messages en temps réel
- Gestion des sockets en Java (TCP)
- Architecture C/S robuste
- Interface console ou interface graphique (selon ton implémentation)
- Notifications de connexion/déconnexion des clients
- Diffusion des messages à tous les utilisateurs connectés

---

## 🧱 Architecture du projet

```

📁 JAVA-CHATROOM
│
├── 📂 server
│   ├── Server.java
│   ├── ClientHandler.java
│   └── ...
│
├── 📂 client
│   ├── Client.java
│   └── ...
│
└── README.md

````

### 🖥️ Serveur
- Démarre un socket serveur (`ServerSocket`)
- Accepte les connexions de clients
- Crée un thread `ClientHandler` par client
- Diffuse les messages à tous les clients connectés

### 🧑‍💻 Client
- Se connecte au serveur via socket TCP
- Envoie les messages au serveur
- Reçoit les messages des autres utilisateurs en temps réel

---

## 🛠️ Technologies utilisées

- **Java 8+**
- **Sockets TCP**
- **Threads**
- **Architecture Client/Serveur**

---

## ▶️ Lancer le projet

### 1️⃣ Lancer le serveur
```bash
java Server
````

### 2️⃣ Lancer un ou plusieurs clients

```bash
java Client
```

Chaque client se connecte automatiquement au serveur via l'adresse IP et le port configurés.

---

## ⚙️ Configuration

Dans ton code, modifie si nécessaire :

```java
String SERVER_IP = "127.0.0.1";
int SERVER_PORT = 5000;
```

Pour fonctionner sur un réseau local, remplace `127.0.0.1` par l’adresse IP de la machine serveur.

---

## 📡 Fonctionnement interne

* Le serveur écoute sur un port TCP.
* Lorsqu’un client se connecte, un thread dédié lui est attribué.
* Chaque message reçu par le serveur est redistribué à tous les clients connectés.
* Le système est basé sur une boucle infinie de lecture/écriture via `BufferedReader` et `PrintWriter`.

---

## 📌 Améliorations possibles

* Ajout d’une interface graphique JavaFX ou Swing
* Sécurisation (SSL, authentification)
* Gestion des pseudos et des couleurs
* Envoi de fichiers
* Historique des messages

---

## 👨‍💻 Auteur

Projet réalisé par **Mohammed Ryad Benyakoub**
📧 Contact : *(mets ton email si tu veux)*

---

## 📜 Licence

Projet libre pour apprentissage et démonstration.
Vous pouvez le modifier, l'améliorer et le distribuer.

```

---

Si tu veux, je peux aussi :  
✅ écrire un README plus professionnel  
✅ ajouter des images ou schémas  
✅ écrire une documentation complète

Souhaites-tu une version améliorée ?
```
