pipeline {
    agent any

    tools {
        jdk "Temurin Java 21"
    }

    triggers {
        githubPush()
    }

    environment {
        DISCORD_WEBHOOK_URL = credentials('polydev-discord-webhook-url')
    }

    stages {
        stage('Checkout') {
            steps {
                scmSkip(deleteBuild: true)
            }
        }

        stage('Setup Gradle') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Build') {
            steps {
                withGradle {
                    sh './gradlew build --rerun-tasks -x check'
                }
            }

            post {
                success {
                    archiveArtifacts artifacts: 'platforms/fabric/build/libs/Terra-fabric*.jar,platforms/bukkit/build/libs/Terra-bukkit*-shaded.jar,platforms/allay/build/libs/Terra-allay*.jar,platforms/minestom/build/libs/Terra-minestom*.jar', fingerprint: true, onlyIfSuccessful: true

                    javadoc javadocDir: 'common/api/build/docs/javadoc', keepAll: true
                }
            }
        }

        stage('Tests') {
            steps {
                withGradle {
                    sh './gradlew test --rerun-tasks'
                }
            }
        }

//         stage('Deploy to snapshots repositories') {
//             when {
//                 allOf {
//                     not { buildingTag() }
//                     not { expression { env.TAG_NAME != null && env.TAG_NAME.matches('v\\d+\\.\\d+\\.\\d+') } }
//                 }
//             }
//
//             steps {
//                 withCredentials([
//                     string(credentialsId: 'maven-signing-key', variable: 'ORG_GRADLE_PROJECT_signingKey'),
//                     string(credentialsId: 'maven-signing-key-password', variable: 'ORG_GRADLE_PROJECT_signingPassword'),
//                     usernamePassword(
//                         credentialsId: 'solo-studios-maven',
//                         passwordVariable: 'ORG_GRADLE_PROJECT_SoloStudiosSnapshotsPassword',
//                         usernameVariable: 'ORG_GRADLE_PROJECT_SoloStudiosSnapshotsUsername'
//                     )
//                 ]) {
//                     withGradle {
//                         sh './gradlew publishAllPublicationsToSoloStudiosSnapshotsRepository'
//                     }
//                 }
//             }
//         }

        stage('Deploy to releases repositories') {
//             when {
//                 allOf {
//                     buildingTag()
//                     expression { env.TAG_NAME != null && env.TAG_NAME.matches('v\\d+\\.\\d+\\.\\d+') }
//                 }
//             }

            steps {
                withCredentials([
                    string(credentialsId: 'maven-signing-key', variable: 'ORG_GRADLE_PROJECT_signingKey'),
                    string(credentialsId: 'maven-signing-key-password', variable: 'ORG_GRADLE_PROJECT_signingPassword'),
                    usernamePassword(
                        credentialsId: 'solo-studios-maven',
                        passwordVariable: 'ORG_GRADLE_PROJECT_SoloStudiosReleasesPassword',
                        usernameVariable: 'ORG_GRADLE_PROJECT_SoloStudiosReleasesUsername'
                    ),
                    // TODO: does not yet exist (uncomment once added)
                    // usernamePassword(
                    //     credentialsId: 'sonatype-maven-credentials',
                    //     passwordVariable: 'ORG_GRADLE_PROJECT_SonatypePassword',
                    //     usernameVariable: 'ORG_GRADLE_PROJECT_SonatypeUsername'
                    // ),
                    // usernamePassword(
                    //     credentialsId: 'codemc-maven-credentials',
                    //     passwordVariable: 'ORG_GRADLE_PROJECT_CodeMCPassword',
                    //     usernameVariable: 'ORG_GRADLE_PROJECT_CodeMCUsername'
                    // )
                ]) {
                    withGradle {
                        sh './gradlew publishAllPublicationsToSoloStudiosReleasesRepository'
                        // sh './gradlew publishAllPublicationsToSonatypeRepository'
                        // sh './gradlew publishAllPublicationsToCodeMCRepository'
                    }
                }
            }
        }
    }

    post {
        always {
            discoverReferenceBuild()

            // junit testResults: '**/build/test-results/*/TEST-*.xml'

            recordIssues(
                aggregatingResults: true,
                enabledForFailure: true,
                minimumSeverity: 'ERROR',
                sourceCodeEncoding: 'UTF-8',
                checksAnnotationScope: 'ALL',
                sourceCodeRetention: 'LAST_BUILD',
                tools: [java(), javaDoc()]
            )

            discordSend(
                title: env.JOB_NAME + ' ' + env.BUILD_DISPLAY_NAME,
                showChangeset: true,
                enableArtifactsList: true,
                link: env.BUILD_URL,
                result: currentBuild.currentResult,
                customAvatarUrl: 'https://github.com/PolyhedralDev.png',
                customUsername: 'Solo Studios Jenkins',
                webhookURL: env.DISCORD_WEBHOOK_URL,
            )

            cleanWs()
        }
    }
}
