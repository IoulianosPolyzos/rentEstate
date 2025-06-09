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

//         stage('run ansible pipeline') {
//             steps {
//                 build job: 'ansible-job'
//             }
//         }

        stage('Checkout Ansible repo') {
                    steps {
                        checkout([$class: 'GitSCM', branches: [[name: 'main']],
                            userRemoteConfigs: [[url: 'https://github.com/IoulianosPolyzos/ansible.git']]])
                    }
                }

        stage('test connection to deploy env') {
        steps {
            sh '''
                ansible -i ~/workspace/ansible/hosts.yaml -m ping appservers,dbservers
            '''
            }
        }

        stage('Install postgres') {
             when {
                expression { return params.INSTALL_POSTGRES }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l dbservers ~/workspace/ansible/playbook/postgres.yaml
                '''
            }
        }

        stage('install springboot') {
             when {
                expression { return params.INSTALL_SPRING }
            }
            steps {
                sh '''
                    export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
                    ansible-playbook -i ~/workspace/ansible/hosts.yaml -l appservers ~/workspace/ansible/playbook/spring.yaml
                '''
            }
        }
}
}