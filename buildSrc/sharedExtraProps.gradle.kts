extra["cmtMvnName"] = providers.systemProperty("cmt.mvn.name").getOrElse(System.getenv("CMT_MVN_NAME"))
extra["cmtMvnUrl"] = providers.systemProperty("cmt.mvn.url").getOrElse(System.getenv("CMT_MVN_URL"))
extra["cmtMvnUsername"] = providers.systemProperty("cmt.mvn.username").getOrElse(System.getenv("CMT_MVN_USERNAME"))
extra["cmtMvnPassword"] = providers.systemProperty("cmt.mvn.password").getOrElse(System.getenv("CMT_MVN_PASSWORD"))
