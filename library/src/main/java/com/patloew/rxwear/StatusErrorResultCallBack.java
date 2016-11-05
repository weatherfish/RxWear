package com.patloew.rxwear;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import rx.Subscriber;

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
class StatusErrorResultCallBack implements ResultCallback<Status> {

    private final Subscriber subscriber;

    StatusErrorResultCallBack(@NonNull Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onResult(@NonNull Status status) {
        if (!status.isSuccess()) {
            subscriber.onError(new StatusException(status));
        }
    }
}
