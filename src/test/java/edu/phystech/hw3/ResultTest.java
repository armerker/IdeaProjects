package edu.phystech.hw3;


import edu.phystech.hw3.result.Failure;
import edu.phystech.hw3.result.Result;
import edu.phystech.hw3.result.ResultUtil;
import edu.phystech.hw3.result.Success;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


public class Result<T> {
    private final T value;
    private final Throwable exception;
    private final boolean isSuccess;
    
    private Result(T value, Throwable exception, boolean isSuccess) {
        this.value = value;
        this.exception = exception;
        this.isSuccess = isSuccess;
    }
    
    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true);
    }
    
    public static <T> Result<T> failure(Throwable exception) {
        return new Result<>(null, exception, false);
    }
    
    public boolean isSuccess() {
        return isSuccess;
    }
    
    public boolean isFailure() {
        return !isSuccess;
    }
    
    public T getOrDefault(T defaultValue) {
        return isSuccess ? value : defaultValue;
    }
    
    public Throwable getExceptionOrNull() {
        return isFailure ? exception : null;
    }
    
    public <U> Result<U> map(Function<T, U> mapper) {
        if (isSuccess) {
            return success(mapper.apply(value));
        }
        return failure(exception);
    }
    
    @Override
    public String toString() {
        if (isSuccess) {
            return "Success(" + value + ")";
        }
        return "Failure(" + exception + ")";
    }
}

class ResultUtil {
    public static <T> Result<T> execute(Supplier<T> supplier) {
        try {
            return Result.success(supplier.get());
        } catch (Throwable t) {
            return Result.failure(t);
        }
    }
}

public class ResultTest {

    @Test
    public void successWorks() {
        int x = 11;
        Result<Integer> result = ResultUtil.execute(() -> x * x);

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertFalse(result.isFailure());

        Result<String> stringResult = result.map(Object::toString);
        Assertions.assertTrue(stringResult.isSuccess());
        Assertions.assertFalse(stringResult.isFailure());

        Assertions.assertEquals("121", stringResult.getOrDefault(""));
    }

    @Test
    public void failureWorks() {
        RuntimeException e = new RuntimeException("Something went wrong!");
        Result<Integer> result = ResultUtil.execute(() -> {
            throw e;
        });

        Assertions.assertFalse(result.isSuccess());
        Assertions.assertTrue(result.isFailure());

        Result<String> mapResult = result.map(x -> {
            Assertions.fail("This lambda shouldn't be executed");
            return null;
        });

        Assertions.assertFalse(result.isSuccess());
        Assertions.assertTrue(result.isFailure());

        Assertions.assertEquals("", mapResult.getOrDefault(""));
        Assertions.assertEquals(e, mapResult.getExceptionOrNull());
    }

    // Для того, чтобы этот тест отработал, нужно поменять определение классов Success & Failure
    // (данная функциональность появилась с java19)
//
//    @Test
//    public void resultPatternDeconstructionWorks() {
//        int x = 11;
//        Result<Integer> result = ResultUtil.execute(() -> x * x);
//
//        Integer a = switch (result) {
//            case Success(var value) -> value;
//            case Failure(Throwable e) -> Assertions.fail();
//        };
//
//        Result<Integer> failureResult = ResultUtil.execute(() -> {
//            throw new RuntimeException(":(");
//        });
//
//        Integer b = switch (failureResult) {
//            case Success(var value) -> Assertions.fail();
//            case Failure(Throwable e) -> {
//                Assertions.assertEquals(":(", e.getMessage());
//                yield 1;
//            }
//        };
//    }
}
