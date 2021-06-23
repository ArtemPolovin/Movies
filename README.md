For creating the Movie app I used Clean architecture and MVVM architecture pattern. Also I used third-party libraries such as:

⦁	Retrofit - for making HTTP request and fetching data from remote server

⦁	Coroutines - for processing fetched data from remote server in background.

⦁	DuggerHilt - for avoiding hurd coupling between objects.

⦁	Paging V3 library - to load elements and display them on the screen on a page-by-page  and  avoid loading a large number of elements per one request

⦁	Glide - for loading images from internet using  url

⦁	RecyclerView - for displaying element on screen and scrolling  them

⦁	Navigation component - for navigation between fragments

For design I chose RecyclerView for displaying movies on home page, I added Animations to recyclerView and to navigation between fragments.

<img src="https://user-images.githubusercontent.com/65748653/123181812-ed5a9a00-d442-11eb-9267-479a100f53ac.jpg" width="100" height="200"

![Screenshot_20210623-161707_Movies](https://user-images.githubusercontent.com/65748653/123181812-ed5a9a00-d442-11eb-9267-479a100f53ac.jpg)
