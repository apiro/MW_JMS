# Project JMS for the course Middleware Technologies for Distributed Systems, year 2016-17

[Course Page](http://corsi.dei.polimi.it/distsys/)

The project has been deployed using Glassfish 4.1.
The following JMS queues need to be created in glassfish:
- tweetQueue
- saveQueue
- thumbnailQueue
- requestQueue

## Project description
In this project you need to develop the backend of a social networking application called Instatweet.

Instatweet allows you to share text and images with your followers. The way the system works is that when a "consuming" user connects to Instatweet he/she sees an up-to-date version of his/her timeline containing their friends' tweets with thumbnail versions of any images they shared. If the "consuming" user clicks on one of the images the original full-size version is shown.

Instead of generating a new up-to-date timeline every time a "consuming" user connects to the system, Instatweet keeps each user's timeline always up-to-date (regardless of whether the user is connected or not). This is done by adding new messages and images to the timelines they need to be in, as soon as they are shared to the system by the "publishing" user.

The system uses queues to decouple the "publishing" user from the "consuming" user, both in space and time. In fact, the "publishing" user does not need to wait for all his/her followers' timelines to be updated. Instead, control is immediately given back to the user as soon as the new message/image enters the system.

The system also uses queues to coordinate the updates to the users' timelines, the storing of any images attached to the messages, as well as the creation of the thumbnail versions of the attached images and their storage.

For extra points: Use Queue browsers to initiate horizontal scaling whenever posting activities grow to the point in which they start causing load problems.