package com.lindseyweberc196.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.lindseyweberc196.DAO.AssessmentDAO;
import com.lindseyweberc196.DAO.CourseDAO;
import com.lindseyweberc196.DAO.TermDAO;
import com.lindseyweberc196.Entity.Assessment;
import com.lindseyweberc196.Entity.Course;
import com.lindseyweberc196.Entity.Term;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1, exportSchema = false)
@TypeConverters({StatusConverter.class, AssessmentConverter.class})
public abstract class EducationManagementDatabase extends RoomDatabase {

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile  EducationManagementDatabase INSTANCE;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(1);

    static EducationManagementDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EducationManagementDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), EducationManagementDatabase.class, "Education_Management_Database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }



    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                TermDAO mTermDao = INSTANCE.termDAO();
                mTermDao.deleteAllTerms();
//                mTermDao.deleteAllTerms();
//                mCourseDao.deleteAllCourses();
//                mAssessmentDao.deleteAllAssessments();

                Term term = new Term(1,"Term 1","05/01/2020","12/01/2020");
                mTermDao.insert(term);
                term = new Term(2,"Term 2","12/01/2020","05/01/2021");
                mTermDao.insert(term);
                term = new Term(3,"Term 3","05/01/2021","12/01/2021");
                mTermDao.insert(term);
            });
        }
    };





    //Add temporary data to DB
//    class PopulateDBAsync extends AsyncTask<Void, Void, Void> {
//
//        private final TermDAO mTermDao;
//        private final CourseDAO mCourseDao;
//        private final AssessmentDAO mAssessmentDao;
//
//        PopulateDBAsync(EducationManagementDatabase db) {
//            mTermDao = db.termDAO();
//            mCourseDao = db.courseDAO();
//            mAssessmentDao = db.assessmentDAO();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//            // Start the app with a clean database every time.
//            // Not needed if you only populate on creation.
//            mTermDao.deleteAllTerms();
//            mCourseDao.deleteAllCourses();
//            mAssessmentDao.deleteAllAssessments();
//
//            Term term = new Term(1,"Term 1","05/01/2020","12/01/2020");
//            mTermDao.insert(term);
//            term = new Term(2,"Term 2","12/01/2020","05/01/2021");
//            mTermDao.insert(term);
//            term = new Term(3,"Term 3","05/01/2021","12/01/2021");
//            mTermDao.insert(term);
//
//        Course course = new Course(1,1,"Mobile Application", "05/01/2020", "06/01/2020", Course.Status.INPROGRESS, "Tim", "727=867-5309", "email@email.com", "This is a note.");
//        mCourseDao.insert(course);
//        course = new Course(2,"trike",8.0);
//        mCourseDao.insert(course);
//
//            Assessment assessment = new Assessment(1,"bike",6.0);
//            mAssessmentDao.insert(assessment);
//            assessment = new Assessment(2,"trike",8.0);
//            mAssessmentDao.insert(assessment);
//
//            return null;
//        }
//    }


}