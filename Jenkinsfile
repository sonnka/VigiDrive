pipeline {
	agent any

	stages {

        stage('Build Docker') {
            steps {
                bat 'docker-compose -f docker-compose.yml up -d --build'
            }
        }
	}
}