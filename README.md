# CRAPPY MUSIC VISUALIZER
<hr>

### made using Java(WIP)

- this is a rapidly put together music visualizer made using java and:
- [fft4j by tombaApp](https://github.com/tambapps/fourier-transform-library)
- [jcodec](https://github.com/jcodec/jcodec)
- currently supports only `.wav` files 
## Build Dependencies
```xml
        <dependency>
            <groupId>com.tambapps.fft4j</groupId>
            <artifactId>fft4j</artifactId>
            <version>2.0</version>
        </dependency>
        <dependency>
            <groupId>org.jcodec</groupId>
            <artifactId>jcodec</artifactId>
            <version>0.2.5</version>
        </dependency>
            <dependency>
            <groupId>org.jcodec</groupId>
            <artifactId>jcodec-javase</artifactId>
            <version>0.2.5</version>
        </dependency>
```

### TO-DO List
- [x] basic image generation behavior
    - [x] chunk size calculated on a FPS target
    - [ ] center image
    - [ ] foreground rotation
    - [ ] background image
        - [ ] background image blue
- [x] (Slow)Save to a video
  - [ ] with FFmpeg
    - [ ] with sound
- [ ] User Interface
  - [x] basic command line control
    - [ ] More options
  - [ ] basic GUI