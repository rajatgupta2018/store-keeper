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

package com.example.store_keeper.store_keeper.ui.suppliers.config;

import android.widget.ImageView;

import com.example.store_keeper.store_keeper.data.local.models.ProductLite;
import com.example.store_keeper.store_keeper.ui.products.config.ProductConfigActivity;

/**
 * Interface to be implemented by {@link SupplierConfigActivityFragment}
 * to receive callback events for User actions on RecyclerView list of Supplier Products
 *
 * @author Rajat Gupta And Harshita Joshi 
 */
public interface SupplierProductsUserActionsListener {

    /**
     * Callback Method of {@link SupplierProductsUserActionsListener} invoked when
     * the user clicks on "Edit" button or the Item View itself. This should
     * launch the {@link ProductConfigActivity}
     * for the Product to be edited.
     *
     * @param itemPosition          The adapter position of the Item clicked.
     * @param product               The {@link ProductLite} associated with the Item clicked.
     * @param imageViewProductPhoto The ImageView of the Adapter Item that displays the Image
     */
    void onEditProduct(final int itemPosition, ProductLite product, ImageView imageViewProductPhoto);

    /**
     * Callback Method of {@link SupplierProductsUserActionsListener} invoked when
     * the user swipes the Item View to remove the Supplier-Product link.
     *
     * @param itemPosition The adapter position of the Item swiped.
     * @param product      The {@link ProductLite} associated with the Item swiped.
     */
    void onSwiped(final int itemPosition, ProductLite product);
}
