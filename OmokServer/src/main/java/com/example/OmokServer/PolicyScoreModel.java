package com.example.OmokServer;

import org.tensorflow.*;
import org.tensorflow.op.Ops;
import org.tensorflow.op.core.Placeholder;
import org.tensorflow.op.math.Add;
import org.tensorflow.types.TFloat32;
import org.tensorflow.types.TInt32;

public class PolicyScoreModel {

    private final String PATH = "C:\\Users\\keror\\JINWOOK\\OmokProject\\OmokServer\\src\\main\\java\\com\\example\\OmokServer\\gomoku_model";

    public void test(){
        try(SavedModelBundle model = SavedModelBundle.load(PATH, "serve")){
            // make input tensor
            Tensor<TFloat32> input = TFloat32.vectorOf(new float[]{1.0f, 2.0f, 3.0f});

            //
            Tensor<TFloat32> output = (Tensor<TFloat32>) model.session()
                    .runner()
                    .feed("input_tensor", input)
                    .fetch("output_tensor")
                    .run()
                    .get(0);

            System.out.println(output);
        }
    }

    public void predict(int[][] board){
        try(SavedModelBundle model = SavedModelBundle.load(PATH, "serve")){
            // board로 ndarray를 초기화
            //Tensor<TInt32> input = TInt32.tensorOf();
        }
    }

    public void idontknowwhatthefuckisthis(){
        try (ConcreteFunction dbl = ConcreteFunction.create(PolicyScoreModel::dbl);
             Tensor<TInt32> x = TInt32.scalarOf(10);
             Tensor<TInt32> dblX = dbl.call(x).expect(TInt32.DTYPE)) {
            System.out.println(x.data().getInt() + " doubled is " + dblX.data().getInt());
        }
    }

    private static Signature dbl(Ops tf) {
        Placeholder<TInt32> x = tf.placeholder(TInt32.DTYPE);
        Add<TInt32> dblX = tf.math.add(x, x);
        return Signature.builder().input("x", x).output("dbl", dblX).build();
    }

}