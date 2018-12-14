/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.pump.activity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.android.pump.R;
import com.android.pump.db.Audio;
import com.android.pump.util.Clog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media2.UriMediaItem;
import androidx.media2.widget.VideoView;

@UiThread
public class AudioPlayerActivity extends AppCompatActivity {
    private static final String TAG = Clog.tag(AudioPlayerActivity.class);

    private VideoView mVideoView;

    public static void start(@NonNull Context context, @NonNull Audio audio) {
        // TODO Find a better URI (audio.getUri()?)
        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                audio.getId());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setDataAndTypeAndNormalize(uri, audio.getMimeType());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        mVideoView = findViewById(R.id.video_view);

        handleIntent();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri == null) {
            Clog.e(TAG, "The intent has no uri. Finishing activity...");
            finish();
            return;
        }
        UriMediaItem mediaItem = new UriMediaItem.Builder(this, uri).build();
        mVideoView.setMediaItem(mediaItem);
    }
}
