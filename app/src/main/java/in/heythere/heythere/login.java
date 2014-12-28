package in.heythere.heythere;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class login extends ActionBarActivity {
    private EditText username_input = null;
    private EditText password_input = null;
    private Button loginbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username_input = (EditText) findViewById(R.id.userid);
        password_input = (EditText) findViewById(R.id.password);
        loginbut = (Button) findViewById(R.id.but_signin);
    }


    public void logintry(View view) {

        String username = username_input.getText().toString();
        String password = password_input.getText().toString();
       /** new  logintrybg(this).execute(username,password);**/
        }

    }

