package com.outlay.firebase;

import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.outlay.data.source.CategoryDataSource;
import com.outlay.domain.model.Category;
import com.outlay.domain.model.User;
import com.outlay.firebase.dto.CategoryDto;
import com.outlay.firebase.dto.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class CategoryFirebaseSource implements CategoryDataSource {

    private DatabaseReference mDatabase;
    private CategoryAdapter adapter;
    private User currentUser;

    @Inject
    public CategoryFirebaseSource(
            User currentUser,
            DatabaseReference databaseReference
    ) {
        this.currentUser = currentUser;
        mDatabase = databaseReference;
        adapter = new CategoryAdapter();

        if (currentUser != null) {
            mDatabase.child("users").child(currentUser.getId()).keepSynced(true);
        }
    }

    @Override
    public Observable<List<Category>> getAll() {
        return Observable.create(subscriber -> {
            mDatabase.child("users").child(currentUser.getId()).child("categories").orderByChild("order")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Category> categories = new ArrayList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                CategoryDto categoryDto = postSnapshot.getValue(CategoryDto.class);
                                categories.add(adapter.toCategory(categoryDto));
                            }
                            subscriber.onNext(categories);
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });


    }

    @Override
    public Observable<Category> getById(String id) {
        return Observable.create(subscriber -> {
            mDatabase.child("users").child(currentUser.getId()).child("categories").child(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            CategoryDto categoryDto = dataSnapshot.getValue(CategoryDto.class);
                            subscriber.onNext(adapter.toCategory(categoryDto));
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            subscriber.onError(databaseError.toException());
                        }
                    });
        });
    }

    @Override
    public Observable<List<Category>> updateAll(List<Category> categories) {
        return Observable.create(subscriber -> {
            List<CategoryDto> categoryDtos = adapter.fromCategories(categories);
            Map<String, Object> categoryDtoMap = new HashMap<>();
            for (CategoryDto categoryDto : categoryDtos) {
                categoryDtoMap.put(categoryDto.getId(), categoryDto);
            }
            Task<Void> task = mDatabase.child("users").child(currentUser.getId())
                    .child("categories")
                    .updateChildren(categoryDtoMap);
            task.addOnCompleteListener(resultTask -> {
                if (task.isSuccessful()) {
                    subscriber.onNext(categories);
                    subscriber.onCompleted();
                } else {
                    Exception e = task.getException();
                    subscriber.onError(e);
                }
            });
        });
    }

    @Override
    public Observable<Category> save(Category category) {
        Observable<Category> saveCategory = Observable.create(subscriber -> {
            CategoryDto categoryDto = adapter.fromCategory(category);

            String key = category.getId();
            if (TextUtils.isEmpty(key)) {
                key = mDatabase.child("users").child(currentUser.getId()).child("categories").push().getKey();
                categoryDto.setId(key);
                category.setId(key);
            }

            Task<Void> task = mDatabase.child("users").child(currentUser.getId())
                    .child("categories").child(key)
                    .setValue(categoryDto);
            task.addOnCompleteListener(resultTask -> {
                if (task.isSuccessful()) {
                    subscriber.onNext(category);
                    subscriber.onCompleted();
                } else {
                    Exception e = task.getException();
                    subscriber.onError(e);
                }
            });
        });
        return saveCategory;
    }

    @Override
    public Observable<Category> remove(Category category) {
        return Observable.create(subscriber -> {
            CategoryDto categoryDto = adapter.fromCategory(category);

            Task<Void> task = mDatabase.child("users").child(currentUser.getId())
                    .child("categories").child(categoryDto.getId())
                    .removeValue();
            task.addOnCompleteListener(resultTask -> {
                if (task.isSuccessful()) {
                    subscriber.onNext(category);
                    subscriber.onCompleted();
                } else {
                    Exception e = task.getException();
                    subscriber.onError(e);
                }
            });
        });
    }

    @Override
    public Observable<Void> clear() {
        throw new UnsupportedOperationException();
    }

}
