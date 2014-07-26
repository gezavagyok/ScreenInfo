package hu.geza.screeninfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class InfoActivity extends Activity {

    private StringBuilder mEmailMsgBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        prepareMsg();
    }

    private void prepareMsg() {
        mEmailMsgBuilder = new StringBuilder();
        mEmailMsgBuilder.append("Here are the information collected about the screen:\n\n");
        printTo(R.id.field1,"Here are the information collected about the screen:");
        // works on everything
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        mEmailMsgBuilder.append(String.format("old width = %d\nold height = %d\n",width,height));
        printTo(R.id.field2,String.format("old width = %d\nold height = %d\n",width,height));
        // find out density
        switch (getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                mEmailMsgBuilder.append("density: LDPI");
                printTo(R.id.field3,"density: LDPI");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                mEmailMsgBuilder.append("density: MDPI");
                printTo(R.id.field3,"density: MDPI");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                mEmailMsgBuilder.append("density: HDPI");
                printTo(R.id.field3,"density: HDPI");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                mEmailMsgBuilder.append("density: XHDPI");
                printTo(R.id.field3,"density: XHDPI");
                break;
            default:
                mEmailMsgBuilder.append("unknown desnity");
                printTo(R.id.field3,"unknown density");
        }

        int widthPixels;
        int heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            Point realSize = new Point();
            Display d = getWindowManager().getDefaultDisplay();
            d.getRealSize(realSize);
            widthPixels = realSize.x;
            heightPixels = realSize.y;
            mEmailMsgBuilder.append(String.format("old width = %d\nold height = %d\n", widthPixels, heightPixels));
            printTo(R.id.field4,String.format("old width = %d\nold height = %d\n", widthPixels, heightPixels));
        }

        // device class
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 720) {
            //Device is a 10" tablet
            mEmailMsgBuilder.append("device is a 10\" tablet\n");
            printTo(R.id.field5,"device is a 10\" tablet");
        }
        else if (smallestWidth > 600) {
            //Device is a 7" tablet
            mEmailMsgBuilder.append("device is a 7\" tablet");
            printTo(R.id.field5,"device is a 7\" tablet");
        }
    }

    private void printTo(int id, String txt){
        ((TextView)findViewById(id)).setText(txt);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_send) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, "geza.csorba@barre.hu");
            intent.putExtra(Intent.EXTRA_SUBJECT, "screen report");
            intent.putExtra(Intent.EXTRA_TEXT, mEmailMsgBuilder.toString());

            startActivity(Intent.createChooser(intent, "Send Report"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
