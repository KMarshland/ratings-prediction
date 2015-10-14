# Ratings Prediction
This is an assignment for Stanford CS46N to predict movie ratings. Given a set of training data, we must predict how a given set of users will rate the given movies. For more information, see here: http://web.stanford.edu/class/cs46n/assignments.htm

## Strategies
When predicting a rating, we will combine three factors: the average rating of that movie, how similar users rated that movie, and how that user rated similar movies. The weights of these three factors can be experimentally determined by testing a wide range of values and seeing how well it does at predicting a subset of the training data.

Determining similar users and movies could be done using a distance algorithm. For example, users with the same gender and age might have a distance of zero, whereas users with a different gender and the same age would have a distance of one. The weights of the factors in the distance algorithms can be experimentally determined as well.
 
Applying the distance algorithm could be done in one of three ways. The first option is to use a k nearest neighbors clustering algorithm, and use the average rating of the movie in that bucket. Another option is to take the k most similar users/movies, and average those. Finally, we could take all the movies/users with distance less than k. This k could be experimentally determined, as could which method to use.

Obviously, this strategy requires a large number (16 so far) of coefficients to be determined experimentally. It will likely take too much time to test every combination, so the best option might be to use an evolutionary algorithm to find the best values.  

## Technical
We're using JDK 1.8.0_60. Given that the body of data is relatively small, the information is currently held in memory rather than a database, though this may change.
