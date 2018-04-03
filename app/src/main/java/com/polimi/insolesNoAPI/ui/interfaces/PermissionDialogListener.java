package com.polimi.insolesNoAPI.ui.interfaces;

import android.support.v4.app.DialogFragment;

public interface PermissionDialogListener {
    void onDialogPositiveClick(DialogFragment dialog);
    void onDialogNegativeClick(DialogFragment dialog);
}