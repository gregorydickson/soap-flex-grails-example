
quartz {
    autoStartup = true
    jdbcStore = false
    waitForJobsToCompleteOnShutdown = false
    exposeSchedulerInRepository = false

    props {
        scheduler.skipUpdateCheck = true
        threadPool.threadCount=100
    }
}

environments {
    
    production {
        quartz {
            autoStartup = true
        }
    }
    
}
