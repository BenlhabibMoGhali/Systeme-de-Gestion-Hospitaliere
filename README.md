# 🏥 Projet Java – Système de Gestion Hospitalière
# Realisé par:
-Fassi Fihri Mouad
-Benlhabib Mohamed El Ghali

## 📌 Description
Ce projet consiste à développer une application **Java** de **gestion hospitalière**.  
Il permet de gérer les patients, le personnel médical, les rendez-vous, les urgences, les consultations, la facturation, les chambres, la pharmacie et la persistance des données.

Le projet met en pratique les concepts avancés de la **programmation orientée objet**, ainsi que plusieurs **design patterns**.

---

## 🎯 Objectifs du projet
- Appliquer les principes de la programmation orientée objet (OOP)
- Concevoir une architecture claire et modulaire
- Implémenter plusieurs design patterns
- Gérer des scénarios métier réels (hôpital)
- Manipuler des collections, enums, interfaces et threads
- Implémenter la persistance des données
- Générer des statistiques métier

---

## 🛠️ Technologies utilisées
- **Langage** : Java (Java 11+)
- **IDE** : IntelliJ IDEA
- **Diagrammes UML** : PlantUML
- **Versioning** : Git & GitHub
- **Persistance** : fichiers CSV
- **Paradigmes** : Programmation Orientée Objet

---

## 🧩 Architecture du projet

Le projet est organisé en packages pour assurer une séparation claire des responsabilités :
src/
├── main/
├── manager/
├── model/
├── service/
├── observer/
├── command/
├── state/
├── strategy/
├── composite/
├── exception/

---

## 🧠 Design Patterns utilisés
- **Singleton** : `HospitalManager`
- **Factory** : `RendezVousFactory`
- **Strategy** : stratégies de facturation
- **Observer** : notifications système
- **Command** : annulation de rendez-vous
- **State** : états des rendez-vous
- **Composite** : hiérarchie des services médicaux

---

## 📦 Fonctionnalités principales

### 👤 Gestion des patients
- Création et gestion des patients
- Dossier médical complet
- Historique des consultations
- Dossier d’urgence

### 🧑‍⚕️ Gestion du personnel médical
- Médecins, infirmiers, pharmaciens, administrateurs
- Gestion des agendas médicaux
- Médecins de garde

### 📅 Gestion des rendez-vous
- Prise de rendez-vous
- Annulation et report
- États du rendez-vous (Programmé, En cours, Terminé, Annulé)
- Rappels automatiques (thread)

### 🚨 Gestion des urgences
- File d’attente prioritaire (PriorityQueue)
- Triage par niveau d’urgence
- Affectation automatique du médecin de garde

### 🛏️ Gestion des chambres et lits
- Attribution automatique selon l’urgence
- Transfert de patients
- Libération et nettoyage des lits

### 💊 Gestion de la pharmacie
- Gestion des stocks
- Alertes de stock faible
- Commandes automatiques
- Traçabilité des médicaments contrôlés
- Thread de surveillance

### 💳 Facturation
- Calcul automatique des factures
- Paiements partiels
- Stratégies de couverture (assurance)
- Génération de reçus

### 📊 Statistiques
- Consultations par médecin
- Consultations par spécialité
- Médicaments les plus prescrits
- Revenus totaux

### 💾 Persistance des données
- Sauvegarde et chargement :
  - Patients
  - Chambres
  - Médicaments
- Stockage via fichiers CSV

---

## 📐 Diagrammes UML réalisés
- Diagramme de classes
- Diagramme de packages
- Diagramme de séquence (prise de rendez-vous)
- Diagramme d’état (rendez-vous)
- Diagramme de composants (services)

---

## ▶️ Instructions d’exécution

1. Cloner le dépôt :
```bash
git clone https://github.com/BenlhabibMoGhali/Systeme-de-Gestion-Hospitaliere

