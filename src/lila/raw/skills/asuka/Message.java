package lila.raw.skills.asuka;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class Message extends ImageView{

	public Message(Context context) {
		super(context);
		this.setBackground(context.getResources().getDrawable(R.drawable.message));
	}

}
