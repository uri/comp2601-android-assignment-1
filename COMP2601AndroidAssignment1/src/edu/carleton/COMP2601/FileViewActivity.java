package edu.carleton.COMP2601;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FileViewActivity extends Activity {

	private CharSequence content;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.file_view);
	    
	    TextView text = (TextView)findViewById(R.id.fileView);
	    
	    Bundle extras = getIntent().getExtras();
	    if(extras != null) {
	    	content =extras.getCharSequence("content");
	    }
	    
	    if (content != null) {
	    	text.setText(content);
	    }
	
	}

}
