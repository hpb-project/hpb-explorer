/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hpb.bc.event;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by davidroon on 19.08.16.
 * This code is released under Apache 2 license
 */
public class AbstractHandler<T> implements ObservableOnSubscribe<T> {
    public final Observable<T> observable;
    private final Set<ObservableEmitter<? super T>> emitters = ConcurrentHashMap.newKeySet();

    public AbstractHandler() {
        observable = Observable.create(this);
    }

    @Override
    public void subscribe(ObservableEmitter<T> observableEmitter) throws Exception {
        emitters.add(observableEmitter);
        removeDisposed();
    }

    public void on(final T param) {
        newElement(param);
    }

    public void newElement(final T param) {
        removeDisposed();
        emitters.forEach(emitter -> {
            try {
                if (emitter.isDisposed()) {
                    emitter.onComplete();
                    emitters.remove(emitter);
                } else {
                    emitter.onNext(param);
                }
            } catch (Throwable ex) {
                emitter.onError(ex);
            }
        });
    }

    private void removeDisposed() {
        Set<ObservableEmitter<? super T>> disposed = emitters.stream()
                .filter(ObservableEmitter::isDisposed)
                .collect(Collectors.toSet());
        emitters.removeAll(disposed);
    }

}
