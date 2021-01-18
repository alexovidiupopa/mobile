package ro.alexpopa.exams.service;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ro.alexpopa.exams.model.Exam;

public interface ApiService {
    String ENDPOINT = "http://192.168.0.106:2018";

    @GET("/exams")
    Observable<List<Exam>> getAllExams();

    @GET("/draft")
    Observable<List<Exam>> getDraftedExams();

    @GET("/group/{name}")
    Observable<List<Exam>> getExamsForGroup(@Path("name") String name);

    @GET("/exam/{id}")
    Observable<Exam> getExamDetails(@Path("id") int id);

    @DELETE("/document/{id}")
    Observable<Exam> deleteDocument(@Path("id") int id);

    @POST("/exam")
    Observable<Exam> registerExam(@Body Exam doc);

    @POST("/join")
    Observable<Exam> joinExam(@Body Exam doc);


}
