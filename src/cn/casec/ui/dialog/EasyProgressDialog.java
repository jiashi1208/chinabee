package cn.casec.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class EasyProgressDialog extends Dialog
{
  private Context mContext;
  private int mLayoutId;
  private String mMessage;

  public EasyProgressDialog(Context paramContext)
  {
    this(paramContext, 0, 0);
  }

  public EasyProgressDialog(Context paramContext, int paramInt1, int paramInt2)
  {
    super(paramContext, paramInt1);
    this.mContext = paramContext;
    WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
    localLayoutParams.width = -1;
    localLayoutParams.height = -1;
    getWindow().setAttributes(localLayoutParams);
    this.mLayoutId = paramInt2;
  }

  public EasyProgressDialog(Context paramContext, int paramInt, String paramString)
  {
    this(paramContext, 2131492895, paramInt);
    setMessage(paramString);
  }

  public EasyProgressDialog(Context paramContext, String paramString)
  {
    this(paramContext, 2131492895, 2130903116);
    setMessage(paramString);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(this.mLayoutId);
    if (TextUtils.isEmpty(this.mMessage))
      return;
    TextView localTextView = (TextView)findViewById(2131231034);
    localTextView.setVisibility(0);
    localTextView.setText(this.mMessage);
  }

  public void setMessage(String paramString)
  {
    this.mMessage = paramString;
  }
}