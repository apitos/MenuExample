package com.geekbrains.menuexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<String> elementsActivated, elementsSimple;
    ArrayAdapter<String> adapterActivated, adapterSimple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Button buttonAP = (Button) findViewById(R.id.ap_button);
        buttonAP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActivatedPopup(view);
            }
        });

        Button buttonSP = (Button) findViewById(R.id.sp_button);
        buttonSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSimplePopup(view);
            }
        });

        //Создаем массив элементов для списка
        elementsActivated = new ArrayList<String>();
        for(int i = 0; i < 5; i++) {
            elementsActivated.add("Element " + i);
        }
        elementsSimple = new ArrayList<String>();
        for(int i = 5; i < 10; i++) {
            elementsSimple.add("Element " + i);
        }

        // Связываемся с ListView
        final ListView activateListView = (ListView) findViewById(R.id.activate_list);
        ListView simpleListView = (ListView) findViewById(R.id.simple_list);

        // создаем адаптер
        adapterActivated = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_activated_1, elementsActivated);
        adapterSimple = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, elementsSimple);

        // устанавливаем адаптер списку
        activateListView.setAdapter(adapterActivated);
        simpleListView.setAdapter(adapterSimple);

        registerForContextMenu(simpleListView);

        activateListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        activateListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                //обработчик выделения пунктов списка ActionMode
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // обработка нажатия на пункт ActionMode
                long[] ids = activateListView.getCheckedItemIds();
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        if(ids.length!=0)
                            Toast.makeText(ListActivity.this, "Edit" + String.valueOf(ids[0]), Toast.LENGTH_LONG).show();
                        for(long i : ids) editAElement((int) i);
                        mode.finish();
                        return true;
                    case R.id.menu_delete:
                        if(ids.length!=0)
                            Toast.makeText(ListActivity.this, "Delete "+String.valueOf(ids[0]), Toast.LENGTH_LONG).show();
                        for(long i : ids) deleteAElement((int) i);
                        mode.finish();
                        return true;
                    default:
                        mode.finish();
                        return false;
                }
                //mode.finish();
                //return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Устанавливаем для ActionMode меню
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // вызывается при закрытии ActionMode
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // вызывается при обновлении ActionMode
                // true если меню или ActionMode обновлено иначе false
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // обработка нажатий
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Toast.makeText(this, "Add", Toast.LENGTH_LONG).show();
                addSElement();
                return true;
            case R.id.menu_clear:
                Toast.makeText(this, "Clear", Toast.LENGTH_LONG).show();
                clearSList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Метод, который вызывается не всего один раз как было с option menu, а каждый раз перед тем,
    // как context-ное меню будет показано.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    // Метод вызывается по нажатию на любой пункт меню. В качестве агрумента приходит item меню.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_edit:
                editSElement(info.position);
                return true;
            case R.id.menu_delete:
                deleteSElement(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    // Метод, который создает инстанс pop-up menu, надувает его и показывает.
    public void showActivatedPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add:
                        addAElement();
                        return true;
                    case R.id.menu_clear:
                        clearAList();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.main_menu);
        popup.show();
    }

    public void showSimplePopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_add:
                        addSElement();
                        return true;
                    case R.id.menu_clear:
                        clearSList();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.main_menu);
        popup.show();
    }

    private void clearAList() {
        elementsActivated.clear();
        adapterActivated.notifyDataSetChanged();
    }

    private void addAElement() {
        elementsActivated.add("New AL element");
        adapterActivated.notifyDataSetChanged();
    }

    private void clearSList() {
        elementsSimple.clear();
        adapterSimple.notifyDataSetChanged();
    }

    private void addSElement() {
        elementsSimple.add("New SL element");
        adapterSimple.notifyDataSetChanged();
    }

    // Метод переписывает текст пункта меню на другой.
    private void editAElement(int id) {
        elementsActivated.set(id, "Edited");
        adapterActivated.notifyDataSetChanged();
    }

    // Метод удаляет пункт из меню.
    private void deleteAElement(int id) {
        elementsActivated.remove(id);
        adapterActivated.notifyDataSetChanged();
    }

    // Метод переписывает текст пункта меню на другой.
    private void editSElement(int id) {
        elementsSimple.set(id, "Edited");
        adapterSimple.notifyDataSetChanged();
    }

    // Метод удаляет пункт из меню.
    private void deleteSElement(int id) {
        elementsSimple.remove(id);
        adapterSimple.notifyDataSetChanged();
    }
}
