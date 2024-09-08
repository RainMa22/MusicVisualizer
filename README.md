# CRAPPY MUSIC VISUALIZER
<hr>

### made using Java(WIP)

- this is a rapidly put together music visualizer made using java and:
- [Apache Commons Math3](https://commons.apache.org/proper/commons-math/)
- [Apache Commons Lang3](https://commons.apache.org/proper/commons-lang/)
- [jcodec](https://github.com/jcodec/jcodec)
- currently supports only `.wav` files 
## Build Dependencies
```xml
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-math3</artifactId>
            <version>3.6.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>3.17.0</version>
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
> Apache Commons Math is licensed under Apache Licenses 2.0

> Apache Commons Lang is licensed under Apache Licenses 2.0

> jcodec is licensed under BSD 2-Clause License

## Testing Dependencies
```xml
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>5.9.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.9.1</version>
            <scope>test</scope>
        </dependency>
```
> The JUnit Library is licensed under Eclipse Public License v1.0
## TO-DO List
- [x] basic image generation behavior
    - [x] chunk size calculated on a FPS target
      - [ ] user specified chunk size support
    - [x] center image
    - [x] foreground rotation
    - [x] background image
        - [x] background image blur
          - [x] Acceleration with FFT
- [x] (Slow)Save to a video
  - [x] with FFmpeg
    - [x] with sound
- [ ] User Interface
  - [x] basic command line control
    - [ ] More options
  - [ ] basic GUI
- Alternative Rendering Style