class QRectCreator {
    constructor(view,shapeType) {
        // console.log("constructor " + shapeType)

        this.shapeType = shapeType
        this.rect = {
            pt1: {x: 0, y: 0},
            pt2: {x: 0, y: 0}
        }
        this.started = false

        this.view = view
        let ctrl = this
        view.onmousedown = function(event) { ctrl.mousedown(event) }
        view.onmousemove = function(event) { ctrl.mousemove(event) }
        view.onmouseup = function(event) { ctrl.mouseup(event) }
        view.onkeydown = function(event) { ctrl.keydown(event) }
    }

    stop() {
        let view = this.view
        view.onmousedown = null
        view.onmousemove = null
        view.onmouseup = null
        view.onkeydown = null
    }

    reset() {
        let view = this.view
        this.started = false
        view.invalidateRect(this.rect)
        view.fireControllerReset()
    }
    
    buildShape() {
        let rect = this.rect
        let r = normalizeRect(rect)

        // what's diffrent?
        // qview's style and shape's style
        let style = defaultStyle.clone();
        // let style = qview.style

        switch (this.shapeType) {
        case "line":
            return new QLine(rect.pt1, rect.pt2, style)
        case "rect":
            return new QRect(r, style)
        case "ellipse":
            let rx = r.width / 2
            let ry = r.height / 2
            return new QEllipse(r.x + rx, r.y + ry, rx, ry, style)
        case "circle":
            let rc = Math.sqrt(r.width * r.width + r.height * r.height)
            return new QEllipse(rect.pt1.x, rect.pt1.y, rc, rc, style)
        default:
            alert("unknown shapeType: " + this.shapeType)
            return null
        }
    }  


    onpaint(ctx) {
        // console.log("rect onpait :" + this.shapeType)
        if (this.started) {
            this.buildShape().onpaint(ctx)
        }
    }
    
    mousedown(event) {
        // console.log("rect mousedown  :" + this.shapeType)
        this.rect.pt1 = this.view.getMousePos(event)
        this.started = true
    }
    
    mousemove(event) {
        if (this.started) {
            let view = this.view
            this.rect.pt2 = view.getMousePos(event)
            view.invalidateRect(this.rect)
            // console.log("ctrl mousemove:" + this.rect.p2.x + " == " + this.rect.p2.y)
        }
    }
    
    mouseup(event) {
        // console.log("rect mouseup  :" + this.shapeType)
        if (this.started) {
            this.rect.pt2 = this.view.getMousePos(event)
            this.view.doc.addShape(this.buildShape())
            this.reset()
        }
    }

    keydown(event) {
        if (event.keyCode == 27) { // keyEsc
            this.reset()
        }
    }
}


onViewAdded(function(view) {
    view.registerController("LineCreator", function() {
        return new QRectCreator(view, "line")
    })
    view.registerController("RectCreator", function() {
        return new QRectCreator(view, "rect")
    })
    view.registerController("EllipseCreator", function() {
        return new QRectCreator(view, "ellipse")
    })
    view.registerController("CircleCreator", function() {
        return new QRectCreator(view, "circle")
    })
})

// qview.registerController("LineCreator",new QRectCreator("line"))
// qview.registerController("RectCreator",new QRectCreator("rect"))
// qview.registerController("EllipseCreator",new QRectCreator("ellipse"))
// qview.registerController("CircleCreator",new QRectCreator("circle"))