package com.example.shawn.bubblenote;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;



public class Head extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int SHARE_ID = Menu.FIRST + 2;
    private static final int PRO_ID = Menu.FIRST + 3;
    private NoteDbAdapter mDbHelper;
    private Cursor mNotesCursor;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_list);
        mDbHelper = new NoteDbAdapter(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());
    }

    private void fillData() {
        mNotesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(mNotesCursor);

        String[] Dick = new String[]{NoteDbAdapter.KEY_TITLE};
        int[] Jane = new int[]{R.id.text1};
        SimpleCursorAdapter notes =
                new SimpleCursorAdapter(this, R.layout.note_row, mNotesCursor, Dick, Jane);
        setListAdapter(notes);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                createNote();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        menu.add(1, SHARE_ID, 1, R.string.menu_share);
        menu.add(2, PRO_ID, 2, R.string.protect);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteNote(info.id);
                fillData();
                return true;
            case SHARE_ID:
                String[] note = new String[]{NoteDbAdapter.KEY_BODY};
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, note);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case PRO_ID:
                Intent ball = new Intent(this, Password.class);
                startActivity(ball);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mNotesCursor;
        c.moveToPosition(position);
        Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NoteDbAdapter.KEY_ROWID, id);
        i.putExtra(NoteDbAdapter.KEY_TITLE, c.getString(
                c.getColumnIndexOrThrow(NoteDbAdapter.KEY_TITLE)));
        i.putExtra(NoteDbAdapter.KEY_BODY, c.getString(
                c.getColumnIndexOrThrow(NoteDbAdapter.KEY_BODY)));
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        switch(requestCode) {
            case ACTIVITY_CREATE:
                String title = extras.getString(NoteDbAdapter.KEY_TITLE);
                String body = extras.getString(NoteDbAdapter.KEY_BODY);
                mDbHelper.createNote(title, body);
                fillData();
                break;
            case ACTIVITY_EDIT:
                Long rowId = extras.getLong(NoteDbAdapter.KEY_ROWID);
                if (rowId != null) {
                    String editTitle = extras.getString(NoteDbAdapter.KEY_TITLE);
                    String editBody = extras.getString(NoteDbAdapter.KEY_BODY);
                    mDbHelper.updateNote(rowId, editTitle, editBody);
                }
                fillData();
                break;
        }
    }
    public void onBackPressed() {
        Intent i = new Intent(Head.this, BubbleNote.class);
        startActivity(i);
    }
}