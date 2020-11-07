/*
 * Created By Rajat Gupta And Harshita Joshi 
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
 * limitations under the License.
 */

package com.example.store_keeper.store_keeper.data.local.contracts;

import android.net.Uri;

/**
 * Marker interface for the constants that identify the Content Provider
 *
 * @author Rajat Gupta And Harshita Joshi 
 */
public interface StoreContract {

    //The Authority constant of the content provider
    String CONTENT_AUTHORITY = "com.example.store_keeper.storeapp.provider";

    //The Base URI constant to contact the content provider
    Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
}
