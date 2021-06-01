package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
//        Метод `UserMealsUtil.filteredByCycles` должен возвращать записи между startTime и endTime,
//        а поле UserMealWithExcess.excess должно показывать, превышает ли сумма калорий за весь день значение caloriesPerDay
        List<UserMealWithExcess> filteredList = new ArrayList<>();
        Map<LocalDate, Integer> sumOfCalories = new HashMap<>();
        for(UserMeal userMeal : meals){
            sumOfCalories.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }
        for(UserMeal userMeal : meals){
            if(TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)){
                filteredList.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), sumOfCalories.get(userMeal.getDateTime().toLocalDate())>caloriesPerDay));
            }
        }
        return filteredList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate, Integer> sumOfCalories = meals.stream()
                .collect(Collectors.toMap(el -> el.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));

        return meals.stream()
                .filter(el -> TimeUtil.isBetweenHalfOpen(el.getDateTime().toLocalTime(), startTime, endTime))
                .map(el -> new UserMealWithExcess(el.getDateTime(), el.getDescription(),
                        el.getCalories(), sumOfCalories.get(el.getDateTime().toLocalDate())>caloriesPerDay)).collect(Collectors.toList());
    }

}
