pipeline {
    agent any
    
    stages {
        stage('GIT') {
            steps {
                git branch: 'dev',  // ← Changé de 'main' à 'dev'
                    changelog: false, 
                    credentialsId: 'jenkins.github', 
                    url: 'https://github.com/zeineb-ferchichi/projetway.git'
            }
        }
        
        stage('MAVEN Build') {
            steps {
                sh 'mvn clean compile package -DskipTests'
            }
        }
    }
}
