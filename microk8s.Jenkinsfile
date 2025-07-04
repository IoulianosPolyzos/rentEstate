pipeline {

agent any

environment {
        DOCKER_USER='ioulianospolyzos'
        DOCKER_SERVER='ghcr.io'
        DOCKER_PREFIX='ghcr.io/ioulianospolyzos/devops-spring'
    }


stages {


    stage('git clone ') {
        steps {
             git url: 'https://github.com/IoulianosPolyzos/ansible.git', branch: 'main'
        }
    }

    stage('git clone') {
            steps {
                dir('app'){
                 git url: 'https://github.com/IoulianosPolyzos/rentEstate.git', branch: 'main'
                 }
            }
        }


    stage('Docker build and push') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f app/Dockerfile app/
                '''

//                 sh '''
//                     echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
//                     docker push $DOCKER_PREFIX --all-tags
//                 '''
            }
        }



    stage('deploy to kubernetes') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    export ANSIBLE_CONFIG=~/workspace/k8s-job/ansible.cfg
                    ansible-playbook -i ~/workspace/k8s-job/hosts.yaml -e new_image=$DOCKER_PREFIX:$TAG ~/workspace/ansible/playbook/k8s-update-spring-deployment.yaml
                '''
            }
        }



}

}
