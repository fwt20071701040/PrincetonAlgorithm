import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

	public static void main(String[] args) {

		RandomizedQueue<String> queue = new RandomizedQueue<String>();
		int num = Integer.parseInt(args[0]);
		int count = 0;
		while (!StdIn.isEmpty()) {

			String item = StdIn.readString();
			if (++count <= num) {

				queue.enqueue(item);
			} else {

				if (StdRandom.uniform(1, count + 1) <= num) {
					queue.dequeue();
					queue.enqueue(item);
				}
			}
		}

		for (String s : queue) {

			if(num == 0)
				break;
			StdOut.println(s);
			num--;
		}
	}
}
