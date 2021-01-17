package uk.toadl3ss.lavalite.utils;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.ShardManager;
import uk.toadl3ss.lavalite.main.Lavalite;
import uk.toadl3ss.lavalite.data.Constants;

public class SetActivity {
    public static Boolean defaultStatus = false;
    public static void SetActivity(ShardManager jda) {
        if (Constants.status.equals("")) {
            defaultStatus = true;
        }
        if (Constants.game.equals("")) {
            defaultStatus = true;
        }
        if (Constants.status.equals("playing")) {
            if (defaultStatus) {
                return;
            }
            jda.setActivity(Activity.playing(Constants.game));
            return;
        }
        if (Constants.status.equals("watching")) {
            if (defaultStatus) {
                return;
            }
            jda.setActivity(Activity.watching(Constants.game));
            return;
        }
        if (Constants.status.equals("listening")) {
            if (defaultStatus) {
                return;
            }
            jda.setActivity(Activity.listening(Constants.game));
            return;
        }
        if (Constants.status.equals("competing")) {
            if (defaultStatus) {
                return;
            }
            jda.setActivity(Activity.competing(Constants.game));
            return;
        }
        if (defaultStatus) {
            jda.setActivity(Activity.playing("Lavalite v" + Lavalite.version));
        }
    }
}