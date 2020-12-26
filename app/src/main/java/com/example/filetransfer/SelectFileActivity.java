package com.example.filetransfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectFileActivity extends AppCompatActivity implements FileListAdapter.IOnClick {

    private RecyclerView rlv_view;
    private Button btn_affirm,btn_not_affirm;
    private List<Map<String, Object>> aList;
    private FileListAdapter fileListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);

        rlv_view = findViewById(R.id.rlv_view);
        btn_affirm = findViewById(R.id.btn_affirm);
        btn_not_affirm = findViewById(R.id.btn_not_affirm);

        rlv_view.setLayoutManager(new LinearLayoutManager(this));

        aList = new ArrayList<>();

        try {
            loadFolderList(GetFilesUtils.getInstance().getBasePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        btn_affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileListAdapter != null) {
                    List<String> files = fileListAdapter.getFiles();
                    if (files == null || files.size() == 0) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("isCompress",true);
                    intent.putExtra("files", (Serializable) files);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
        btn_not_affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileListAdapter != null) {
                    List<String> files = fileListAdapter.getFiles();
                    if (files == null || files.size() == 0) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("isCompress",false);
                    intent.putExtra("files", (Serializable) files);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void loadFolderList(String file) throws IOException {
        List<Map<String, Object>> list = GetFilesUtils.getInstance().getSonNode(file);
        if (list != null) {
            Collections.sort(list, GetFilesUtils.getInstance().defaultOrder());
            aList.clear();
            for (Map<String, Object> map : list) {
                String fileType = (String) map.get(GetFilesUtils.FILE_INFO_TYPE);
                Map<String, Object> gMap = new HashMap<String, Object>();
                if (map.get(GetFilesUtils.FILE_INFO_ISFOLDER).equals(true)) {
                    gMap.put("fIsDir", true);
                    gMap.put("fInfo", map.get(GetFilesUtils.FILE_INFO_NUM_SONDIRS) + " flie " +
                            map.get(GetFilesUtils.FILE_INFO_NUM_SONFILES) + " document ");
                } else {
                    gMap.put("fIsDir", false);
                    gMap.put("fInfo", "size:" + GetFilesUtils.getInstance().getFileSize(map.get(GetFilesUtils.FILE_INFO_PATH).toString()));
                }
                gMap.put("fName", map.get(GetFilesUtils.FILE_INFO_NAME));
                gMap.put("fPath", map.get(GetFilesUtils.FILE_INFO_PATH));
                gMap.put("fileType",fileType);
                aList.add(gMap);
            }
        } else {
            aList.clear();
        }

        fileListAdapter = new FileListAdapter(aList,this);
        rlv_view.setAdapter(fileListAdapter);
        fileListAdapter.setiOnClick(this);
    }

    @Override
    public void iOnClickJump(String filePath) {
        try {
            loadFolderList(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
