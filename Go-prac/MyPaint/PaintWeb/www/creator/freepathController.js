class QFreePathCreator {
    constructor(view) {
        this.points = []
        this.fromPos = {x: 0, y: 0}
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
        view.ondblclick = null
        view.onkeydown = null
    }

    reset() {
        this.points = []
        this.started = false

        let view = this.view
        view.invalidateRect(null)
        view.fireControllerReset()
    }
    
    buildShape() {
        let points = [{x: this.fromPos.x, y: this.fromPos.y}]
        for (let i in this.points) {
            points.push(this.points[i])
        }
        return new QPath(points, this.close, defaultStyle.clone())
    }  


    onpaint(ctx) {
        if (this.started) {
            let props = qview.style
            ctx.lineWidth = props.lineWidth
            ctx.strokeStyle = props.lineColor
            ctx.beginPath()
            ctx.moveTo(this.fromPos.x, this.fromPos.y)
            for (let i in this.points) {
                ctx.lineTo(this.points[i].x, this.points[i].y)
            }
            ctx.stroke()
        }
    }
    
    mousedown(event) {
        this.fromPos = this.view.getMousePos(event)
        this.started = true
    }
    
    mousemove(event) {
        if (this.started) {
            this.points.push(this.view.getMousePos(event))
            this.view.invalidateRect(null)
        }
    }
    
    mouseup(event) {
        if (this.started) {
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
    view.registerController("FreePathCreator", function() {
        return new QFreePathCreator(view)
    })
})

// qview.registerController("FreePathCreator", new QFreePathCreator())