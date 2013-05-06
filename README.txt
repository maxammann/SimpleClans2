This package contains all official modules of SimpleClans2.
The SimpleClansAPI-*.jar is the API! This is only for developers! Do not drop this into your plugins directory!


Known bugs and solutions
- If you get this message:
    - "Failed at inserting clan because it already exists! Please follow the instructions on the jenkins page or on the devbukkit page else your data may get corrupted!"
        1. Exectue the command: /sc save
        2. Exectue the command: /sc copy sqlite SimpleClans2_tmp.db
        3. Copy SimpleClans2_tmp.db from your server's root to plugins/SimpleClans2/SimpleClans2.db (Replace or move an old db if there is one)
        4. Stop the server
        5. !Backup your mysql database! and drop the tables with the prefix sc2_. (For example in phpmyadmin)
        6. Change the settings from mysql to sqlite in the config and start the server (The server should start now and all data should be there!)
        7. Execute /sc copy mysql [host] [db] [user] [pw]
        8. Stop the server again, change settings to mysql and start the server again!

Project page: http://dev.bukkit.org/server-mods/simpleclans/
Jenkins: http://build.greatmancode.com/browse/SIMPLECLANS-SIMPLECLANS2
Javadocs: http://bit.ly/SC-javadocs1
Bug tracker: http://bug.greatmancode.com/browse/SC

