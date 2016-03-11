package com.patloew.rxwear;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

import rx.Observer;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class DataGetItemsObservable extends BaseObservable<DataItem> {

    private final Uri uri;
    private final Integer filterType;

    DataGetItemsObservable(RxWear rxWear, Uri uri, Integer filterType, Long timeout, TimeUnit timeUnit) {
        super(rxWear, timeout, timeUnit);
        this.uri = uri;
        this.filterType = filterType;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super DataItem> observer) {
        ResultCallback<DataItemBuffer> resultCallback = new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(@NonNull DataItemBuffer dataItemBuffer) {
                try {
                    if(!dataItemBuffer.getStatus().isSuccess()) {
                        observer.onError(new StatusException(dataItemBuffer.getStatus()));
                    } else {
                        for (int i = 0; i < dataItemBuffer.getCount(); i++) {
                            observer.onNext(dataItemBuffer.get(i).freeze());
                        }

                        observer.onCompleted();
                    }
                } catch(Throwable throwable) {
                    observer.onError(throwable);
                } finally {
                    dataItemBuffer.release();
                }
            }
        };

        if(uri == null) {
            setupWearPendingResult(Wearable.DataApi.getDataItems(apiClient), resultCallback);
        } else {
            if(filterType == null) {
                setupWearPendingResult(Wearable.DataApi.getDataItems(apiClient, uri), resultCallback);
            } else {
                setupWearPendingResult(Wearable.DataApi.getDataItems(apiClient, uri, filterType), resultCallback);
            }
        }
    }
}