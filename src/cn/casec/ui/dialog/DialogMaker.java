package cn.casec.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

public class DialogMaker
{
  private static EasyProgressDialog progressDialog;

  public static void dismissProgressDialog()
 {
		if (progressDialog == null) {
			return;
		}
		while (!progressDialog.isShowing()) {
			try {
				progressDialog.dismiss();
				progressDialog = null;
				return;
			} catch (Exception localException) {
			}
		}
	}

  public static boolean isShowing()
  {
    return (progressDialog != null) && (progressDialog.isShowing());
  }

  public static void setMessage(String paramString)
  {
    if ((progressDialog == null) || (!progressDialog.isShowing()) || (TextUtils.isEmpty(paramString)))
      return;
    progressDialog.setMessage(paramString);
  }

  public static EasyProgressDialog showProgressDialog(Context paramContext, String paramString)
  {
    return showProgressDialog(paramContext, null, paramString, true, null);
  }

  public static EasyProgressDialog showProgressDialog(Context paramContext, String paramString, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    return showProgressDialog(paramContext, null, paramString, true, paramOnCancelListener);
  }

  @Deprecated
  public static EasyProgressDialog showProgressDialog(Context paramContext, String paramString1, String paramString2, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener)
 {
		if (progressDialog == null)
			progressDialog = new EasyProgressDialog(paramContext, paramString2);
		progressDialog.setCancelable(paramBoolean);
		progressDialog.setOnCancelListener(paramOnCancelListener);
		progressDialog.show();
		return progressDialog;
	}

  public static EasyProgressDialog showProgressDialog(Context paramContext, String paramString, boolean paramBoolean)
  {
    return showProgressDialog(paramContext, null, paramString, paramBoolean, null);
  }
}
