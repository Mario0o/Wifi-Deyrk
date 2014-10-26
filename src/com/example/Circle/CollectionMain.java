package com.example.Circle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.Circle.R;

public class CollectionMain extends ListFragment {
    
	private View rootView;
	private DB mDbHelper; //��@DB���O
    private Cursor mCursor; //���Ʈw��ƪ��e��

    public CollectionMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        rootView = inflater.inflate(R.layout.fragment_collection_main, container, false);
        //�]�w�e���ҹ�����XML��
        
        setAdapter(rootView);
        //��s�e����ListView
        
    	return rootView;
    }
    
    private void setAdapter(View rootView) {
    	mDbHelper = new DB(this.getActivity());
        mDbHelper.open();
        //���}DB
        
        mCursor = mDbHelper.getMylove();
        //�I�sDB��getAll�禡�A���o��Ʃ�imCursor(��Ʈw��ƪ��e��)
        
        ListCursorAdapter cadapter = new ListCursorAdapter(this.getActivity(), mCursor);
        //��@ListCursorAdapter���f(�]�w�n�b�e���W��ܪ�ListView�˦��A�ǤJListView�Ҧb��activity��mCursor�ӳ]�w)
        
        setListAdapter(cadapter);
        //���汵�f
	}
    
    //ListCursorAdapter���f
	public class ListCursorAdapter extends CursorAdapter{
		
		public ListCursorAdapter(Context context,Cursor c){
			super(context,c);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			//�qcursor�̮���ƥX�ө��e���W
			
			TextView title =(TextView)view.findViewById(R.id.title_ad);
			TextView time = (TextView)view.findViewById(R.id.time);
			//ImageView image = (ImageView)view.findViewById(R.id.image_ad);
			
			title.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TITLE)));
			time.setText(cursor.getString(
							cursor.getColumnIndex(DB.KEY_TIME)));
			
			//byte[] bb = cursor.getBlob(cursor.getColumnIndex(DB.KEY_IMAGE));
			//image.setImageBitmap(BitmapFactory.decodeByteArray(bb, 0, bb.length));
			//�qcursor���Ϥ��X�����ɩ�bImageView���{���X
			
			cursor.getColumnIndex(DB.KEY_TIME);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(context);
			View v =inflater.inflate(R.layout.listview_receive_collection,null);
			//�]�wListView�˦�
			bindView(v,context,cursor);
			
			return v;
		}
	}
	
	//��ListView���Q�I���ɹB�檺�禡
	public void onListItemClick(ListView l, View v, int position, long id) {
	    super.onListItemClick(l, v, position, id);
	    
	    FragmentTransaction trans = getFragmentManager().beginTransaction();
	    //trans.addToBackStack("CollectionMain");
        trans.replace(R.id.root_collection, new CollectionAD());
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
        //�Nroot_collection(���ðϪ����h�e��)���e��fragment������CollectionAD
	    
	}
}