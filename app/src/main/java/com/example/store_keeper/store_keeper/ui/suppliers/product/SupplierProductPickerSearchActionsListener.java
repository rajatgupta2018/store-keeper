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

package com.example.store_keeper.store_keeper.ui.suppliers.product;

/**
 * Interface to be implemented by {@link SupplierProductPickerActivity}
 * to receive callback events for User Search actions on the RecyclerView list of Products.
 *
 * @author Rajat Gupta And Harshita Joshi 
 */
public interface SupplierProductPickerSearchActionsListener {
    /**
     * Callback Method of {@link SupplierProductPickerSearchActionsListener} invoked when
     * all the Products available, are already picked for the Supplier. Hence the implementation
     * should disable the Search.
     */
    void disableSearch();
}
