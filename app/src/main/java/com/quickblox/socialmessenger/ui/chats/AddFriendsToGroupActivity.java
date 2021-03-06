package com.quickblox.socialmessenger.ui.chats;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.quickblox.socialmessenger.ui.chats.dialogs.NewDialogCounterFriendsListener;
import com.quickblox.core.core.command.Command;
import com.quickblox.core.db.managers.UsersDatabaseManager;
import com.quickblox.core.models.User;
import com.quickblox.core.models.GroupDialog;
import com.quickblox.core.qb.commands.QBAddFriendsToGroupCommand;
import com.quickblox.core.service.QBServiceConsts;
import com.quickblox.core.utils.FriendUtils;

import java.util.ArrayList;

public class AddFriendsToGroupActivity extends BaseSelectableFriendListActivity implements NewDialogCounterFriendsListener {

    private static final String EXTRA_GROUP_DIALOG = "extra_group_dialog";
    public static final int RESULT_ADDED_FRIENDS = 3;

    private GroupDialog dialog;
    private ArrayList<Integer> friendIdsList;

    public static void start(Activity activity, GroupDialog dialog) {
        Intent intent = new Intent(activity, AddFriendsToGroupActivity.class);
        intent.putExtra(EXTRA_GROUP_DIALOG, dialog);
        activity.startActivityForResult(intent, RESULT_ADDED_FRIENDS);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActions();
    }

    @Override
    protected Cursor getFriends() {
        dialog = (GroupDialog) getIntent().getExtras().getSerializable(EXTRA_GROUP_DIALOG);
        return UsersDatabaseManager.getFriendsFilteredByIds(this, FriendUtils.getFriendIds(
                dialog.getOccupantList()));
    }

    @Override
    protected void onFriendsSelected(ArrayList<User> selectedFriends) {
        showProgress();
        friendIdsList = FriendUtils.getFriendIds(selectedFriends);
        QBAddFriendsToGroupCommand.start(this, dialog.getId(), friendIdsList);
    }

    private void addActions() {
        addAction(QBServiceConsts.ADD_FRIENDS_TO_GROUP_SUCCESS_ACTION, new AddFriendsToGroupSuccessCommand());
        updateBroadcastActionList();
    }

    private class AddFriendsToGroupSuccessCommand implements Command {

        @Override
        public void execute(Bundle bundle) {
            hideProgress();
            Intent intent = new Intent();
            intent.putExtra(QBServiceConsts.EXTRA_FRIENDS, friendIdsList);
            setResult(RESULT_ADDED_FRIENDS, intent);
            finish();
        }
    }
}