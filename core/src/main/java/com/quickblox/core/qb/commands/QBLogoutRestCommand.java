package com.quickblox.core.qb.commands;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.core.command.ServiceCommand;
import com.quickblox.core.qb.helpers.QBAuthHelper;
import com.quickblox.core.service.QBService;
import com.quickblox.core.service.QBServiceConsts;

public class QBLogoutRestCommand extends ServiceCommand {

    private final QBAuthHelper authHelper;

    public QBLogoutRestCommand(Context context, QBAuthHelper authHelper, String successAction,
            String failAction) {
        super(context, successAction, failAction);
        this.authHelper = authHelper;
    }

    public static void start(Context context) {
        Intent intent = new Intent(QBServiceConsts.LOGOUT_REST_ACTION, null, context, QBService.class);
        context.startService(intent);
    }

    @Override
    public Bundle perform(Bundle extras) throws QBResponseException {
        authHelper.logout();
        return extras;
    }
}
