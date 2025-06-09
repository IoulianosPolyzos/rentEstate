pipeline {
    agent any

    parameters {
        booleanParam(name: 'INSTALL_POSTGRES', defaultValue: true, description: 'Install PostgreSQL')
        booleanParam(name: 'INSTALL_SPRING', defaultValue: true, description: 'Install Spring Boot app')
    }

    environment {
        PATH = "/usr/bin:/usr/local/bin:${env.PATH}"
    }

    stages {

        stage('test connection to deploy env') {
            steps {
                sh '''
                    ansible -i $WORKSPACE/hosts.yaml -m ping appservers,dbservers
                '''
            }
        }

        stage('Install postgres') {
            when {
                expression { return params.INSTALL_POSTGRES }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=$WORKSPACE/ansible.cfg
                    ansible-playbook -i $WORKSPACE/hosts.yaml -l dbservers $WORKSPACE/playbooks/postgres-16.yaml
                '''
            }
        }

        stage('install springboot') {
            when {
                expression { return params.INSTALL_SPRING }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=$WORKSPACE/ansible.cfg
                    ansible-playbook -i $WORKSPACE/hosts.yaml -l appservers $WORKSPACE/playbooks/spring.yaml
                '''
            }
        }
    }
}